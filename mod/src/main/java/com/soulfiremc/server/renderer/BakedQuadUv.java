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
package com.soulfiremc.server.renderer;

import net.minecraft.client.model.geom.builders.UVPair;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

final class BakedQuadUv {
  private BakedQuadUv() {
  }

  static float localU(TextureAtlasSprite sprite, long packedUv) {
    var atlasU = UVPair.unpackU(packedUv);
    var span = sprite.getU1() - sprite.getU0();
    if (Math.abs(span) < 1.0E-6F) {
      return 0.0F;
    }

    return Math.clamp((atlasU - sprite.getU0()) / span, 0.0F, 1.0F);
  }

  static float localV(TextureAtlasSprite sprite, long packedUv) {
    var atlasV = UVPair.unpackV(packedUv);
    var span = sprite.getV1() - sprite.getV0();
    if (Math.abs(span) < 1.0E-6F) {
      return 0.0F;
    }

    return Math.clamp((atlasV - sprite.getV0()) / span, 0.0F, 1.0F);
  }
}
