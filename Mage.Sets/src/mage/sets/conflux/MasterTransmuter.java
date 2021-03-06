/*
 *  Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and should not be interpreted as representing official policies, either expressed
 *  or implied, of BetaSteward_at_googlemail.com.
 */
package mage.sets.conflux;

import java.util.UUID;
import mage.constants.CardType;
import mage.constants.Outcome;
import mage.constants.Rarity;
import mage.constants.Zone;
import mage.MageInt;
import mage.abilities.Ability;
import mage.abilities.common.SimpleActivatedAbility;
import mage.abilities.costs.common.ReturnToHandTargetCost;
import mage.abilities.costs.common.TapSourceCost;
import mage.abilities.costs.mana.ManaCostsImpl;
import mage.abilities.effects.OneShotEffect;
import mage.cards.Card;
import mage.cards.CardImpl;
import mage.filter.common.FilterArtifactCard;
import mage.filter.common.FilterControlledArtifactPermanent;
import mage.game.Game;
import mage.players.Player;
import mage.target.Target;
import mage.target.common.TargetCardInHand;
import mage.target.common.TargetControlledPermanent;

/**
 *
 * @author LevelX2
 */
public class MasterTransmuter extends CardImpl {

    public MasterTransmuter(UUID ownerId) {
        super(ownerId, 31, "Master Transmuter", Rarity.RARE, new CardType[]{CardType.ARTIFACT, CardType.CREATURE}, "{3}{U}");
        this.expansionSetCode = "CON";
        this.subtype.add("Human");
        this.subtype.add("Artificer");

        this.power = new MageInt(1);
        this.toughness = new MageInt(2);

        // {U}, {tap}, Return an artifact you control to its owner's hand: You may put an artifact card from your hand onto the battlefield.
        Ability ability = new SimpleActivatedAbility(Zone.BATTLEFIELD, new MasterTransmuterEffect(), new ManaCostsImpl("{U}"));
        ability.addCost(new TapSourceCost());
        ability.addCost(new ReturnToHandTargetCost(new TargetControlledPermanent(new FilterControlledArtifactPermanent("an artifact"))));
        this.addAbility(ability);

    }

    public MasterTransmuter(final MasterTransmuter card) {
        super(card);
    }

    @Override
    public MasterTransmuter copy() {
        return new MasterTransmuter(this);
    }
}

class MasterTransmuterEffect extends OneShotEffect {

    public MasterTransmuterEffect() {
        super(Outcome.Benefit);
        this.staticText = "You may put an artifact card from your hand onto the battlefield";
    }

    public MasterTransmuterEffect(final MasterTransmuterEffect effect) {
        super(effect);
    }

    @Override
    public MasterTransmuterEffect copy() {
        return new MasterTransmuterEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Player controller = game.getPlayer(source.getControllerId());
        if (controller != null) {
            Target target = new TargetCardInHand(new FilterArtifactCard("an artifact card from your hand"));
            if (target.canChoose(source.getSourceId(), source.getControllerId(), game)
                    && controller.chooseUse(outcome, "Put an artifact from your hand to battlefield?", game)
                    && controller.chooseTarget(outcome, target, source, game)) {
                Card card = game.getCard(target.getFirstTarget());
                if (card != null) {
                     controller.putOntoBattlefieldWithInfo(card, game, Zone.HAND, source.getSourceId());
                }
            }
        }

        return false;
    }
}
