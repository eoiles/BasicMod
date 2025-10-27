package GameMaster.cards;

import GameMaster.character.MyCharacter;
import GameMaster.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Cleave extends BaseCard {
    public static final String ID = makeID(Cleave.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ENEMY, // primary target; others take splash
            2 // cost
    );

    private static final int DAMAGE = 12;
    private static final int UPG_DAMAGE = 6; // upgrade to 18; splash becomes 9

    public Cleave() {
        super(ID, info);
        setDamage(DAMAGE, UPG_DAMAGE);
        // (Intentionally not a STRIKE)
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Primary hit
        addToBot(new DamageAction(
                m,
                new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL),
                AbstractGameAction.AttackEffect.SLASH_HEAVY
        ));

        // Splash: half of BASE damage (rounded down), will scale with powers per target
        int splashBase = this.baseDamage / 2;
        if (splashBase <= 0) return;

        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (mo == m || mo.isDeadOrEscaped()) continue;
            addToBot(new DamageAction(
                    mo,
                    new DamageInfo(p, splashBase, DamageInfo.DamageType.NORMAL),
                    AbstractGameAction.AttackEffect.SLASH_HORIZONTAL
            ));
        }
    }

    @Override
    public AbstractCard makeCopy() { return new Cleave(); }
}
