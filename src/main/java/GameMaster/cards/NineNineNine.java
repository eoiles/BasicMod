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

public class NineNineNine extends BaseCard {
    public static final String ID = makeID(NineNineNine.class.getSimpleName());

    // Card info: color, type, rarity, target, cost
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.RARE,       // SPECIAL so it won’t clog reward pools (spawn with console)
            CardTarget.ENEMY,
            999                          // cost
    );

    private static final int DAMAGE = 999;
    private static final int UPG_DAMAGE =9000;         // no upgrade change

    public NineNineNine() {
        super(ID, info);
        setDamage(DAMAGE, UPG_DAMAGE);
        // (no tags needed)
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(
                m,
                new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL),
                AbstractGameAction.AttackEffect.BLUNT_HEAVY
        ));
    }

    @Override
    public AbstractCard makeCopy() {
        return new NineNineNine();
    }
}
