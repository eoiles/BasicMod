package GameMaster.cards;

import GameMaster.character.MyCharacter;
import GameMaster.util.CardStats;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.helpers.CardLibrary;

import java.util.ArrayList;

public class Fate extends BaseCard {
    public static final String ID = makeID(Fate.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            3 // big effect; tune to taste
    );

    private static final int CASTS = 10;
    private static final int UPG_DELTA = 10; // 10 -> 20

    public Fate() {
        super(ID, info);
        setMagic(CASTS, UPG_DELTA); // !M! shows 10 / 20
        setExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int plays = 0;
        int tries = 0;
        final int maxTries = magicNumber * 6; // safety cap

        while (plays < magicNumber && tries < maxTries) {
            tries++;

            AbstractCard choice = randomLibraryCard();
            if (choice == null) continue;

            // Skip this card itself to avoid recursive explosions (optional)
            if (ID.equals(choice.cardID)) continue;

            // Make a fresh, combat-ready copy
            AbstractCard tmp = choice.makeStatEquivalentCopy();
            // Play it for free, and remove it after use (no piles)
            tmp.freeToPlayOnce = true;
            tmp.purgeOnUse = true;
            tmp.setCostForTurn(0);
            tmp.energyOnUse = 0; // X-costs will be 0

            AbstractMonster target = null;
            if (tmp.target == CardTarget.ENEMY) {
                target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
                if (target == null || target.isDeadOrEscaped()) continue;
            }

            addToBot(new NewQueueCardAction(tmp, target, false, true));
            plays++;
        }
    }

    private static AbstractCard randomLibraryCard() {
        // Build once per call to keep it simple; you can cache if desired
        ArrayList<AbstractCard> all = CardLibrary.getAllCards();
        if (all == null || all.isEmpty()) return null;

        // Try a handful of times to avoid curses/status/unplayable
        for (int i = 0; i < 10; i++) {
            AbstractCard c = all.get(AbstractDungeon.cardRandomRng.random(all.size() - 1));
            if (c == null) continue;
            if (c.type == AbstractCard.CardType.CURSE || c.type == AbstractCard.CardType.STATUS) continue;
            if (c.cost == -2) continue; // unplayable
            return c;
        }
        return null;
    }

    @Override
    public AbstractCard makeCopy() { return new Fate(); }
}
