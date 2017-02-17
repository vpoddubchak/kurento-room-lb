/*
 * (C) Copyright 2015 Kurento (http://kurento.org/)
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
 *
 */

package org.kurento.room.kms;

import org.kurento.client.KurentoClient;

public class Kms {

  private LoadManager loadManager = new MaxWebRtcLoadManager(10000);
  private KurentoClient client;
  private String kmsUri;

  public Kms(KurentoClient client, String kmsUri) {
    this.client = client;
    this.kmsUri = kmsUri;
  }

  public void setLoadManager(LoadManager loadManager) {
    this.loadManager = loadManager;
  }

  public double getLoad() {
    return loadManager.calculateLoad(this);
  }

  public boolean allowMoreElements() {
    return loadManager.allowMoreElements(this);
  }

  public String getUri() {
    return kmsUri;
  }

  public KurentoClient getKurentoClient() {
    return this.client;
  }
}
