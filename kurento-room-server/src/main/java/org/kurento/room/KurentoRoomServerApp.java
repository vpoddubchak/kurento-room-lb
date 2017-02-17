/*
 * (C) Copyright 2014 Kurento (http://kurento.org/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kurento.room;

import static org.kurento.commons.PropertiesManager.getPropertyJson;

import java.util.List;

import org.kurento.jsonrpc.JsonUtils;
import org.kurento.jsonrpc.internal.server.config.JsonRpcConfiguration;
import org.kurento.jsonrpc.server.JsonRpcConfigurer;
import org.kurento.jsonrpc.server.JsonRpcHandlerRegistry;
import org.kurento.room.api.KurentoClientProvider;
import org.kurento.room.kms.FixedOneKmsManager;
import org.kurento.room.rpc.JsonRpcNotificationService;
import org.kurento.room.rpc.JsonRpcUserControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import com.google.gson.JsonArray;

/**
 * Room server application.
 *
 * @author Ivan Gracia (izanmail@gmail.com)
 * @author Micael Gallego (micael.gallego@gmail.com)
 * @author Radu Tom Vlad (rvlad@naevatec.com)
 * @since 1.0.0
 */
@Import(JsonRpcConfiguration.class)
@SpringBootApplication
public class KurentoRoomServerApp implements JsonRpcConfigurer {

  public static final String KMSS_URIS_PROPERTY = "kms.uris";
  public static final String KMSS_URIS_DEFAULT = "[ \"ws://localhost:8888/kurento\" ]";

  private static final Logger log = LoggerFactory.getLogger(KurentoRoomServerApp.class);

  @Bean
  @ConditionalOnMissingBean
  public KurentoClientProvider kmsManager() {

    JsonArray kmsUris = getPropertyJson(KMSS_URIS_PROPERTY, KMSS_URIS_DEFAULT, JsonArray.class);
    List<String> kmsWsUris = JsonUtils.toStringList(kmsUris);

    if (kmsWsUris.isEmpty()) {
      throw new IllegalArgumentException(KMSS_URIS_PROPERTY
          + " should contain at least one kms url");
    }

    String firstKmsWsUri = kmsWsUris.get(0);

    if (firstKmsWsUri.equals("autodiscovery")) {
      log.info("Using autodiscovery rules to locate KMS on every pipeline");
      return new AutodiscoveryKurentoClientProvider();
    } else {
      log.info("Configuring Kurento Room Server to use first of the following kmss: " + kmsWsUris);
      return new FixedOneKmsManager(firstKmsWsUri);
    }
  }

  @Bean
  @ConditionalOnMissingBean
  public JsonRpcNotificationService notificationService() {
    return new JsonRpcNotificationService();
  }

  @Bean
  @ConditionalOnMissingBean
  public NotificationRoomManager roomManager() {
    return new NotificationRoomManager(notificationService(), kmsManager());
  }

  @Bean
  @ConditionalOnMissingBean
  public JsonRpcUserControl userControl() {
    return new JsonRpcUserControl(roomManager());
  }

  @Bean
  @ConditionalOnMissingBean
  public RoomJsonRpcHandler roomHandler() {
    return new RoomJsonRpcHandler(userControl(), notificationService());
  }

  @Override
  public void registerJsonRpcHandlers(JsonRpcHandlerRegistry registry) {
    registry.addHandler(roomHandler().withPingWatchdog(true), "/room");
  }

  public static void main(String[] args) throws Exception {
    start(args);
  }

  public static ConfigurableApplicationContext start(String[] args) {
    log.info("Using /dev/urandom for secure random generation");
    System.setProperty("java.security.egd", "file:/dev/./urandom");
    return SpringApplication.run(KurentoRoomServerApp.class, args);
  }
}
