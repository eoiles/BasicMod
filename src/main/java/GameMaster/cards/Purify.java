package GameMaster.cards;

import GameMaster.character.MyCharacter;
import GameMaster.util.CardStats;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;
import java.util.List;

public class Purify extends BaseCard {
    public static final String ID = makeID(Purify.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.ENEMY,
            2 // cost
    );

    public Purify() {
        super(ID, info);
        setExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m == null) return;

        // Collect first to avoid concurrent modification
        List<AbstractPower> buffs = new ArrayList<>();
        for (AbstractPower pw : m.powers) {
            if (pw != null && pw.type == AbstractPower.PowerType.BUFF) {
                buffs.add(pw);
            }
        }
        for (AbstractPower pw : buffs) {
            addToBot(new RemoveSpecificPowerAction(m, p, pw.ID));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            setCostUpgrade(1); // 2 → 1 when upgraded
        }
    }

    @Override
    public AbstractCard makeCopy() { return new Purify(); }
}
