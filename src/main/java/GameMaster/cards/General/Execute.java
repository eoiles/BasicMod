package GameMaster.cards.General;

import GameMaster.cards.BaseCard;
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

    private static final int PERCENT = 10;
    private static final int UPG_PERCENT_DELTA = 10; // 10% -> 20% on upgrade

    public Execute() {
        super(ID, info);
        setDamage(0, 0);              // pure execute; no normal damage
        setMagic(PERCENT, UPG_PERCENT_DELTA);
        setCostUpgrade(0);            // still upgrades to 0 cost
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m == null || m.isDeadOrEscaped()) return;

        int threshold = (int)Math.ceil(m.maxHealth * (magicNumber / 100.0f));
        if (m.currentHealth <= threshold) {
            addToBot(new InstantKillAction(m));
        }
    }

    @Override
    public AbstractCard makeCopy() { return new Execute(); }
}
