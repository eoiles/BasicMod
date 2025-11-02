package GameMaster.cards.General;

import GameMaster.cards.BaseCard;
import GameMaster.character.MyCharacter;
import GameMaster.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Negate extends BaseCard {
    public static final String ID = makeID(Negate.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            1 // cost
    );

    public Negate() {
        super(ID, info);
        setExhaust(true); // avoid per-turn lockouts by default
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m == null || m.isDeadOrEscaped()) return;

        // Force the monster to do nothing (STUN) for its next action.
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                m.setMove((byte)-1, AbstractMonster.Intent.STUN);
                m.createIntent(); // refresh the intent UI
                isDone = true;
            }
        });
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(0); // 1 -> 0
        }
    }

    @Override
    public AbstractCard makeCopy() { return new Negate(); }
}
