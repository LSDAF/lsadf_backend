/*
 * Copyright © 2024-2025 LSDAF
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lsadf.core.infra.logging.filter;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import java.util.List;
import lombok.Setter;
import org.slf4j.Marker;

@Setter
public class MarkerSkippingFilter extends Filter<ILoggingEvent> {

  private String markerToMatch;

  @Override
  public FilterReply decide(ILoggingEvent event) {
    if (markerToMatch == null) {
      return FilterReply.NEUTRAL;
    }
    List<Marker> markers = event.getMarkerList();
    if (markers == null) {
      return FilterReply.NEUTRAL;
    }

    if (markers.stream().anyMatch(marker -> markerToMatch.equals(marker.getName()))) {
      return FilterReply.ACCEPT;
    }

    return FilterReply.DENY;
  }
}
