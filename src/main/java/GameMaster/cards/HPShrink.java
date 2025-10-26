package GameMaster.cards;

import GameMaster.character.MyCharacter;
import GameMaster.util.CardStats;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class HPShrink extends BaseCard {
    public static final String ID = makeID(HPShrink.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.ENEMY,
            2 // cost (change if you want)
    );

    public HPShrink() {
        super(ID, info);
        setExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m == null || m.isDeadOrEscaped()) return;

        int current = m.currentHealth;
        int target = (int) Math.rint(current * 0.1); // round-to-even
        if (target < 0) target = 0;

        int lose = current - target;
        if (lose > 0) {
            // HP loss ignores Block. (Note: Intangible will still reduce damage to 1.)
            addToBot(new LoseHPAction(m, p, lose));
        }
    }

    @Override
    public AbstractCard makeCopy() { return new HPShrink(); }
}
