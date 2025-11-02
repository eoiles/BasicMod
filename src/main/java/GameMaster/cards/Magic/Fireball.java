package GameMaster.cards.Magic;

import GameMaster.cards.BaseCard;
import GameMaster.character.MyCharacter;
import GameMaster.util.CardStats;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Fireball extends BaseCard {
    // Pack-aware ID via your helper (=> "GameMaster:Magic:Fireball")
    public static final String ID = BaseCard.packedID(Fireball.class);

    // color, type, rarity, target, cost
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ENEMY,
            1
    );

    public Fireball() {
        super(ID, info);
        setDamage(8, 3); // base 8, upgrade +3 (=> 11)
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Deal !D! damage with a FIRE hit effect
        addToBot(new DamageAction(
                m,
                new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.FIRE
        ));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Fireball();
    }
}
