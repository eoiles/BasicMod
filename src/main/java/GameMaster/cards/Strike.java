package GameMaster.cards;

import GameMaster.character.MyCharacter;
import GameMaster.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static GameMaster.BasicMod.makeID;

public class Strike extends BaseCard {
    public static final String ID = makeID(Strike.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.BASIC,
            CardTarget.ENEMY,
            1   // cost
    );

    private static final int DAMAGE = 6;
    private static final int UPG_DAMAGE = 0; // no upgrade change

    public Strike() {
        super(ID, info);
        setDamage(DAMAGE, UPG_DAMAGE);
        tags.add(CardTags.STARTER_STRIKE);
        tags.add(CardTags.STRIKE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(
                m,
                new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL),
                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL
        ));
    }

    @Override
    public AbstractCard makeCopy() { return new Strike(); }
}
