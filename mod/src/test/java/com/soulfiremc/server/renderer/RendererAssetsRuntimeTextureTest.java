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

import com.soulfiremc.test.utils.TestBootstrap;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RendererAssetsRuntimeTextureTest {
  @BeforeAll
  static void bootstrap() {
    TestBootstrap.bootstrapForTest();
  }

  @Test
  void detectsDownloadedSkinTextureIdsAsRuntimeTextures() {
    assertTrue(RendererAssets.isRuntimeClientTexturePath(Identifier.withDefaultNamespace("skins/abc123")));
    assertTrue(RendererAssets.isRuntimeClientTexturePath(Identifier.withDefaultNamespace("capes/abc123")));
    assertTrue(RendererAssets.isRuntimeClientTexturePath(Identifier.withDefaultNamespace("elytra/abc123")));
  }

  @Test
  void keepsResourcePackTextureIdsResourceBacked() {
    assertFalse(RendererAssets.isRuntimeClientTexturePath(Identifier.withDefaultNamespace("textures/entity/player/wide/steve.png")));
    assertFalse(RendererAssets.isRuntimeClientTexturePath(Identifier.withDefaultNamespace("block/stone")));
  }

  @Test
  void keepsBinaryAlphaBlendedTexturesOnCutoutPath() {
    var texture = textureWithAlpha(0);
    var renderType = RenderTypes.entityTranslucent(Identifier.withDefaultNamespace("skins/test"));

    assertEquals(
      RendererAssets.AlphaMode.CUTOUT,
      VanillaSubmitCollector.alphaMode(renderType, texture, 0xFFFFFFFF)
    );
  }

  @Test
  void keepsPartialAlphaBlendedTexturesOnTranslucentPath() {
    var texture = textureWithAlpha(128);
    var renderType = RenderTypes.entityTranslucent(Identifier.withDefaultNamespace("skins/test"));

    assertEquals(
      RendererAssets.AlphaMode.TRANSLUCENT,
      VanillaSubmitCollector.alphaMode(renderType, texture, 0xFFFFFFFF)
    );
  }

  @Test
  void classifiesOnlyTheSubmittedUvRegion() {
    var image = new BufferedImage(4, 4, BufferedImage.TYPE_INT_ARGB);
    for (var y = 0; y < image.getHeight(); y++) {
      for (var x = 0; x < image.getWidth(); x++) {
        image.setRGB(x, y, y < 2 ? 0xFFFFFFFF : 0x80FFFFFF);
      }
    }
    var texture = RendererAssets.TextureImage.from(image, null);
    var renderType = RenderTypes.entityTranslucent(Identifier.withDefaultNamespace("skins/test"));
    var opaqueUv = new float[]{0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 0.5F, 0.0F, 0.5F};
    var translucentUv = new float[]{0.0F, 0.5F, 1.0F, 0.5F, 1.0F, 1.0F, 0.0F, 1.0F};

    assertEquals(
      RendererAssets.AlphaMode.OPAQUE,
      VanillaSubmitCollector.alphaMode(renderType, texture, 0xFFFFFFFF, opaqueUv)
    );
    assertEquals(
      RendererAssets.AlphaMode.TRANSLUCENT,
      VanillaSubmitCollector.alphaMode(renderType, texture, 0xFFFFFFFF, translucentUv)
    );
  }

  private RendererAssets.TextureImage textureWithAlpha(int alpha) {
    var image = new BufferedImage(2, 1, BufferedImage.TYPE_INT_ARGB);
    image.setRGB(0, 0, 0xFFFFFFFF);
    image.setRGB(1, 0, (alpha << 24) | 0x00FFFFFF);
    return RendererAssets.TextureImage.from(image, null);
  }
}
