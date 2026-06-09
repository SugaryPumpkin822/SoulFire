/*
 * SoulFire
 * Copyright (C) 2026  AlexProgrammerDE
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.soulfiremc.shared;

import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.util.SortedArrayStringMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProxyCheckLogFilterTest {
  @Test
  void deniesProxyCheckBotLogs() {
    assertTrue(ProxyCheckLogFilter.shouldDeny(eventWithBotAccountName("ProxyCheck")));
  }

  @Test
  void allowsOtherLogs() {
    assertFalse(ProxyCheckLogFilter.shouldDeny(eventWithBotAccountName("RealBot")));
    assertFalse(ProxyCheckLogFilter.shouldDeny(Log4jLogEvent.newBuilder().build()));
  }

  private static Log4jLogEvent eventWithBotAccountName(String accountName) {
    var contextData = new SortedArrayStringMap();
    contextData.putValue(SFLogAppender.SF_BOT_ACCOUNT_NAME, accountName);

    return Log4jLogEvent.newBuilder()
      .setContextData(contextData)
      .build();
  }
}
