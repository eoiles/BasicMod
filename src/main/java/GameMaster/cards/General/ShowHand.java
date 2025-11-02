package GameMaster.cards.General;

import GameMaster.cards.BaseCard;
import GameMaster.character.MyCharacter;
import GameMaster.util.CardStats;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;

public class ShowHand extends BaseCard {
    public static final String ID = BaseCard.packedID(ShowHand.class);

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            1 // cost
    );

    private static final int DMG = 2;
    private static final int UPG_DMG = 1; // 2 -> 3

    public ShowHand() {
        super(ID, info);
        setDamage(DMG, UPG_DMG);
        setExhaust(true); // now exhausts
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m == null || m.isDeadOrEscaped()) return;

        // Initial hit
        addToBot(new DamageAction(
                m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL),
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL));

        // Snapshot remaining cards in hand (after playing this)
        int repeats = p.hand.size();

        // For each card: discard a RANDOM card (no prompt), then hit again
        for (int i = 0; i < repeats; i++) {
            addToBot(new DiscardAction(p, p, 1, true)); // true = random discard
            addToBot(new DamageAction(
                    m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL),
                    AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        }
    }

    @Override
    public AbstractCard makeCopy() { return new ShowHand(); }
}
