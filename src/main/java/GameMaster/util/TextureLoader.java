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

    // ---------- FIXED: Power textures are NOT pack-aware ----------
    // If a power name contains "Pack:Name", we strip to the trailing segment ("Name").
    // If the specific icon is missing, we fall back to the BaseMod default "example.png".
    private static String stripPackSegment(final String powerName) {
        int idx = powerName.lastIndexOf(':');
        return idx >= 0 ? powerName.substring(idx + 1) : powerName;
    }

    public static Texture getPowerTexture(final String powerName) {
        // 1) Try exact file (works for "LossAndOverflow")
        String primary = powerPath(powerName + ".png");
        Texture t = getTextureNull(primary);
        if (t != null) return t;

        // 2) If a pack-like segment snuck in (e.g., "Math:PrimeStack"), use only the trailing name
        String simpleName = stripPackSegment(powerName);
        if (!simpleName.equals(powerName)) {
            String simple = powerPath(simpleName + ".png");
            t = getTextureNull(simple);
            if (t != null) return t;
        }

        // 3) Fallback to BaseMod-style default icon
        String fallback = powerPath("example.png");
        return getTexture(fallback); // let this throw only if your default truly doesn't exist
    }

    public static Texture getHiDefPowerTexture(final String powerName) {
        // Hi-def is optional; mirror the same stripping but return null if not found.
        String primary = powerPath("large/" + powerName + ".png");
        Texture t = getTextureNull(primary);
        if (t != null) return t;

        String simpleName = stripPackSegment(powerName);
        if (!simpleName.equals(powerName)) {
            String simple = powerPath("large/" + simpleName + ".png");
            t = getTextureNull(simple);
            if (t != null) return t;
        }

        // No large icon: return null so BasePower uses the 48px (or fallback) texture.
        return null;
    }
}
