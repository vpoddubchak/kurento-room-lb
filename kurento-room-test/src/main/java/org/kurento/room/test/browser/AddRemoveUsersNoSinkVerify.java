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
 */

package org.kurento.room.test.browser;

import org.junit.Test;
import org.kurento.room.test.RoomFunctionalBrowserTest;
import org.kurento.test.browser.WebPage;

/**
 * Room integration test (basic version).
 *
 * @author Micael Gallego (micael.gallego@gmail.com)
 * @author Radu Tom Vlad (rvlad@naevatec.com)
 * @since 5.0.0
 */
public class AddRemoveUsersNoSinkVerify extends RoomFunctionalBrowserTest<WebPage> {

  public static final int NUM_USERS = 4;

  @Test
  public void test() throws Exception {
    iterParallelUsers(NUM_USERS, ITERATIONS, new UserLifecycle() {
      @Override
      public void run(int numUser, int iteration) throws Exception {
        String userName = getBrowserKey(numUser);

        log.info("User '{}' is joining room '{}'", userName, roomName);
        joinToRoom(numUser, userName, roomName);
        log.info("User '{}' joined room '{}'", userName, roomName);

        sleep(PLAY_TIME);

        log.info("User '{}' is exiting from room '{}'", userName, roomName);
        exitFromRoom(numUser, userName);
        log.info("User '{}' exited from room '{}'", userName, roomName);
      }
    });
  }

}
