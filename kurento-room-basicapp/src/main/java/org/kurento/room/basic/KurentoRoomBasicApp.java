/*
 * (C) Copyright 2016 Kurento (http://kurento.org/)
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
package org.kurento.room.basic;

import org.kurento.commons.ConfigFileManager;
import org.kurento.room.KurentoRoomServerApp;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Import;

/**
 * Basic demo application for Kurento Room, uses the Room Server and the Room Client JS libraries.
 *
 * @author Micael Gallego (micael.gallego@gmail.com)
 * @author Radu Tom Vlad (rvlad@naevatec.com)
 * @since 5.0.0
 */
@Import(KurentoRoomServerApp.class)
public class KurentoRoomBasicApp {

  private final static String BASICAPP_CFG_FILENAME = "kroombasic.conf.json";

  static {
    ConfigFileManager.loadConfigFile(BASICAPP_CFG_FILENAME);
  }

  public static void main(String[] args) {
    SpringApplication.run(KurentoRoomBasicApp.class, args);
  }
}
