package GameMaster.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.HashMap;
import java.util.Locale;

import static GameMaster.BasicMod.*;

public class TextureLoader {
    private static final HashMap<String, Texture> textures = new HashMap<>();

    public static Texture getTexture(final String filePath) { return getTexture(filePath, true); }

    public static Texture getTexture(final String filePath, boolean linear) {
        if (textures.get(filePath) == null) {
            loadTexture(filePath, linear); // let it throw if truly missing
        }
        Texture t = textures.get(filePath);
        if (t != null && t.getTextureObjectHandle() == 0) {
            textures.remove(filePath);
            t = getTexture(filePath, linear);
        }
        return t;
    }

    public static Texture getTextureNull(final String filePath) { return getTextureNull(filePath, true); }

    public static Texture getTextureNull(final String filePath, boolean linear) {
        if (!textures.containsKey(filePath)) {
            try {
                loadTexture(filePath, linear);
            } catch (GdxRuntimeException e) {
                textures.put(filePath, null);
            }
        }
        Texture t = textures.get(filePath);
        if (t != null && t.getTextureObjectHandle() == 0) {
            textures.remove(filePath);
            t = getTextureNull(filePath, linear);
        }
        return t;
    }

    // --------- PACK-AWARE CARD ART PATH RESOLUTION ----------
    /**
     * Accepts a name of the form "Pack:CardName" (required).
     * Returns a path that EXISTS:
     *   images/cards/<Pack>/<CardName>.png
     *   -> fallback images/cards/<Pack>/default.png
     *   -> fallback images/cards/General/default.png
     *
     * If no pack prefix present, throws to fail fast.
     */
    public static String getCardTextureString(final String cardName, final AbstractCard.CardType cardType) {
        // Require pack prefix like "General:Strike" or "Magic:Fireball"
        int sep = cardName.indexOf(':');
        if (sep < 0) {
            throw new GdxRuntimeException("Card art requested without pack prefix: \"" + cardName + "\"");
        }

        final String pack = cardName.substring(0, sep);
        final String name = cardName.substring(sep + 1);

        // primary
        String small = imagePath("cards/" + pack + "/" + name + ".png");
        if (exists(small)) return small;

        // per-pack default
        String packDefault = imagePath("cards/" + pack + "/default.png");
        if (exists(packDefault)) return packDefault;

        // final fallback
        String generalDefault = imagePath("cards/General/default.png");
        if (exists(generalDefault)) return generalDefault;

        // Nothing exists: throw (so the crash points at the real missing asset)
        throw new GdxRuntimeException(
                "Missing card art for " + pack + ":" + name +
                        " and no defaults at cards/" + pack + "/default.png or cards/General/default.png");
    }

    private static boolean exists(String internalPath) {
        try {
            FileHandle h = Gdx.files.internal(internalPath);
            return h != null && h.exists();
        } catch (Exception e) {
            return false;
        }
    }
    // --------------------------------------------------------

    private static void loadTexture(final String textureString) throws GdxRuntimeException { loadTexture(textureString, false); }

    private static void loadTexture(final String textureString, boolean linearFilter) throws GdxRuntimeException {
        Texture texture = new Texture(textureString);
        if (linearFilter) {
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        } else {
            texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        }
        logger.info("Loaded texture " + textureString);
        textures.put(textureString, texture);
    }

    public static Texture getPowerTexture(final String powerName) {
        String textureString = powerPath(powerName + ".png");
        return getTexture(textureString);
    }
    public static Texture getHiDefPowerTexture(final String powerName) {
        String textureString = powerPath("large/" + powerName + ".png");
        return getTextureNull(textureString);
    }
}
