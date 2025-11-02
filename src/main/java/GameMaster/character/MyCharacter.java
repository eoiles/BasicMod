package GameMaster.character;

import GameMaster.relics.BloodGold;
import basemod.BaseMod;
import basemod.abstracts.CustomEnergyOrb;
import basemod.abstracts.CustomPlayer;
import basemod.animations.SpriterAnimation;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.Strike_Red;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.screens.CharSelectInfo;

import java.util.ArrayList;

import static GameMaster.BasicMod.characterPath;
import static GameMaster.BasicMod.makeID;

public class MyCharacter extends CustomPlayer {
    //Stats
    public static final int ENERGY_PER_TURN = 3;
    public static final int MAX_HP = 99;
    public static final int STARTING_GOLD = 99;
    public static final int CARD_DRAW = 5;
    public static final int ORB_SLOTS = 0;

    //Strings
    private static final String ID = makeID("GameMaster"); // must match CharacterStrings.json key
    private static String[] getNames() { return CardCrawlGame.languagePack.getCharacterString(ID).NAMES; }
    private static String[] getText()  { return CardCrawlGame.languagePack.getCharacterString(ID).TEXT; }

    public static class Meta {
        @SpireEnum public static PlayerClass YOUR_CHARACTER;
        @SpireEnum(name = "CHARACTER_GRAY_COLOR") public static AbstractCard.CardColor CARD_COLOR;
        @SpireEnum(name = "CHARACTER_GRAY_COLOR") @SuppressWarnings("unused")
        public static CardLibrary.LibraryType LIBRARY_COLOR;

        private static final String CHAR_SELECT_BUTTON   = characterPath("select/button.png");
        private static final String CHAR_SELECT_PORTRAIT = characterPath("select/portrait.png");

        private static final String BG_ATTACK  = characterPath("cardback/bg_attack.png");
        private static final String BG_ATTACK_P= characterPath("cardback/bg_attack_p.png");
        private static final String BG_SKILL   = characterPath("cardback/bg_skill.png");
        private static final String BG_SKILL_P = characterPath("cardback/bg_skill_p.png");
        private static final String BG_POWER   = characterPath("cardback/bg_power.png");
        private static final String BG_POWER_P = characterPath("cardback/bg_power_p.png");
        private static final String ENERGY_ORB = characterPath("cardback/energy_orb.png");
        private static final String ENERGY_ORB_P=characterPath("cardback/energy_orb_p.png");
        private static final String SMALL_ORB  = characterPath("cardback/small_orb.png");

        private static final Color cardColor = new Color(128f/255f,128f/255f,128f/255f,1f);

        public static void registerColor() {
            BaseMod.addColor(CARD_COLOR, cardColor,
                    BG_ATTACK, BG_SKILL, BG_POWER, ENERGY_ORB,
                    BG_ATTACK_P, BG_SKILL_P, BG_POWER_P, ENERGY_ORB_P,
                    SMALL_ORB);
        }

        public static void registerCharacter() {
            BaseMod.addCharacter(new MyCharacter(), CHAR_SELECT_BUTTON, CHAR_SELECT_PORTRAIT);
        }
    }

    private static final String SHOULDER_1 = characterPath("shoulder.png");
    private static final String SHOULDER_2 = characterPath("shoulder2.png");
    private static final String CORPSE     = characterPath("corpse.png");

    private static final String[] orbTextures = {
            characterPath("energyorb/layer1.png"),
            characterPath("energyorb/layer2.png"),
            characterPath("energyorb/layer3.png"),
            characterPath("energyorb/layer4.png"),
            characterPath("energyorb/layer5.png"),
            characterPath("energyorb/cover.png"),
            characterPath("energyorb/layer1d.png"),
            characterPath("energyorb/layer2d.png"),
            characterPath("energyorb/layer3d.png"),
            characterPath("energyorb/layer4d.png"),
            characterPath("energyorb/layer5d.png")
    };

    private static final float[] layerSpeeds = new float[] { -20.0F, 20.0F, -40.0F, 40.0F, 360.0F };

    public MyCharacter() {
        super(getNames()[0], Meta.YOUR_CHARACTER,
                new CustomEnergyOrb(orbTextures, characterPath("energyorb/vfx.png"), layerSpeeds),
                new SpriterAnimation(characterPath("animation/default.scml")));

        initializeClass(null, SHOULDER_2, SHOULDER_1, CORPSE, getLoadout(),
                20.0F, -20.0F, 200.0F, 250.0F, new EnergyManager(ENERGY_PER_TURN));

        dialogX = (drawX + 0.0F * Settings.scale);
        dialogY = (drawY + 220.0F * Settings.scale);
    }

    // pass-through helper: expects "Pack:Name"
    private static String g(String packAndName) {
        return makeID(packAndName);
    }

    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();

        // 4x Strike from the General pack
        retVal.add(g("General:Strike"));
        retVal.add(g("General:Strike"));
        retVal.add(g("General:Strike"));
        retVal.add(g("General:Strike"));

        // 4x Defend from the General pack
        retVal.add(g("General:Defend"));
        retVal.add(g("General:Defend"));
        retVal.add(g("General:Defend"));
        retVal.add(g("General:Defend"));

        // character-specific starters (also in General pack)
        retVal.add(g("General:DamageIncrease"));
        retVal.add(g("General:BlockIncrease"));
        retVal.add(g("General:EnergyDecrease"));

        return retVal;
    }

    @Override
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(BloodGold.ID);
        return retVal;
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        return new Strike_Red();
    }

    @Override public int getAscensionMaxHPLoss() { return 4; }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[] {
                AbstractGameAction.AttackEffect.SLASH_VERTICAL,
                AbstractGameAction.AttackEffect.SLASH_HEAVY,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY
        };
    }

    private final Color cardRenderColor = Color.LIGHT_GRAY.cpy();
    private final Color cardTrailColor  = Color.LIGHT_GRAY.cpy();
    private final Color slashAttackColor= Color.LIGHT_GRAY.cpy();
    @Override public Color getCardRenderColor() { return cardRenderColor; }
    @Override public Color getCardTrailColor()  { return cardTrailColor;  }
    @Override public Color getSlashAttackColor(){ return slashAttackColor; }

    @Override public BitmapFont getEnergyNumFont() { return FontHelper.energyNumFontRed; }

    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.playA("ATTACK_DAGGER_2", MathUtils.random(-0.2F, 0.2F));
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, false);
    }
    @Override public String getCustomModeCharacterButtonSoundKey() { return "ATTACK_DAGGER_2"; }

    @Override public String getLocalizedCharacterName() { return getNames()[0]; }
    @Override public String getTitle(PlayerClass playerClass) { return getNames()[1]; }
    @Override public String getSpireHeartText() { return getText()[1]; }
    @Override public String getVampireText() { return getText()[2]; }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(getNames()[0], getText()[0],
                MAX_HP, MAX_HP, ORB_SLOTS, STARTING_GOLD, CARD_DRAW, this,
                getStartingRelics(), getStartingDeck(), false);
    }

    @Override public AbstractCard.CardColor getCardColor() { return Meta.CARD_COLOR; }

    @Override public AbstractPlayer newInstance() { return new MyCharacter(); }
}
