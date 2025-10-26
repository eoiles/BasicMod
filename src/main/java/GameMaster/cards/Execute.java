package GameMaster.cards;

import GameMaster.character.MyCharacter;
import GameMaster.util.CardStats;
import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Execute extends BaseCard {
    public static final String ID = makeID(Execute.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            1 // cost
    );

    public Execute() {
        super(ID, info);
        setDamage(0, 0);        // no normal damage; pure execute check
        setCostUpgrade(0);      // upgrade -> 0 cost (optional, easy knob)
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m == null || m.isDeadOrEscaped()) return;

        // Kill if target has 10% HP or less (threshold uses ceil so exactly 10% qualifies)
        int threshold = (int) Math.ceil(m.maxHealth * 0.10f);
        if (m.currentHealth <= threshold) {
            addToBot(new InstantKillAction(m));
        }
        // Otherwise, no effect (per your spec). If you want chip damage, set a base damage and add a DamageAction here.
    }

    @Override
    public AbstractCard makeCopy() { return new Execute(); }
}
