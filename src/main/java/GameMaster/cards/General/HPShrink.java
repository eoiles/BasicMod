package GameMaster.cards.General;

import GameMaster.cards.BaseCard;
import GameMaster.character.MyCharacter;
import GameMaster.util.CardStats;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class HPShrink extends BaseCard {
    public static final String ID = BaseCard.packedID(HPShrink.class);

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.ENEMY,
            2 // cost
    );

    private static final int PERCENT = 75;     // base: set to 75% of current HP
    private static final int UPGRADE_TO = 50;  // upgrade: set to 50%

    public HPShrink() {
        super(ID, info);
        setExhaust(true);
        // Use magicNumber as the target percent of current HP.
        setMagic(PERCENT, 0); // we'll override upgrade() to set to 50%
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m == null || m.isDeadOrEscaped()) return;

        int current = m.currentHealth;
        int target = (int) Math.rint(current * (magicNumber / 100.0)); // banker’s rounding
        if (target < 0) target = 0;
        if (target > current) target = current; // safety guard

        int lose = current - target;
        if (lose > 0) {
            // HP loss ignores Block (Intangible will still cap it to 1).
            addToBot(new LoseHPAction(m, p, lose));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            // Reduce the percent from 75 -> 50 on upgrade.
            int delta = UPGRADE_TO - baseMagicNumber; // 50 - 75 = -25
            upgradeMagicNumber(delta);
        }
    }

    @Override
    public AbstractCard makeCopy() { return new HPShrink(); }
}
