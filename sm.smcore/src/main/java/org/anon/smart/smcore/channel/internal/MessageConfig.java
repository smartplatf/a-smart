/**
 * SMART - State Machine ARchiTecture
 *
 * Copyright (C) 2012 Individual contributors as indicated by
 * the @authors tag
 *
 * This file is a part of SMART.
 *
 * SMART is a free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SMART is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * */
 
/**
 * ************************************************************
 * HEADERS
 * ************************************************************
 * File:                org.anon.smart.smcore.events.internal.MessageConfig
 * Author:              vjaasti
 * Revision:            1.0
 * Date:                May 8, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * <Purpose>
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.channel.internal;

import static org.anon.utilities.objservices.ObjectServiceLocator.execute;

import java.util.ArrayList;
import java.util.List;

import org.anon.smart.channels.data.DScope;
import org.anon.smart.channels.distill.Distillate;
import org.anon.smart.channels.distill.Rectifier;
import org.anon.smart.channels.distill.RectifierUnit;
import org.anon.smart.channels.shell.DataInstincts;
import org.anon.smart.channels.shell.InternalConfig;
import org.anon.smart.channels.shell.SCFactory;
import org.anon.smart.channels.shell.SCType;
import org.anon.smart.smcore.channel.distill.alteration.AlterationStage;
import org.anon.smart.smcore.channel.distill.sanitization.SanitizationStage;
import org.anon.smart.smcore.channel.distill.storage.StorageStage;
import org.anon.smart.smcore.channel.distill.translation.TranslationStage;
import org.anon.smart.smcore.channel.server.EventErrorHandler;
import org.anon.smart.smcore.events.SmartEvent;
import org.anon.utilities.concurrency.ExecutionUnit;
import org.anon.utilities.exception.CtxException;
import org.anon.utilities.objservices.ConvertService.translator;

public class MessageConfig implements InternalConfig {

	private Rectifier _rectifier;
	private Distillate _alteredMessage;
	public MessageConfig(SmartEvent message) 
        throws CtxException
	{
		System.out.println("Created MessageConfig............");
		_rectifier = new Rectifier();
        addSteps(_rectifier);
        //_rectifier.addStep(new StorageStage());
        MessageErrorHandler ehandler = new MessageErrorHandler();
        _rectifier.setupErrorHandler(ehandler);
        
        InternalMessageDataFactory fact = new InternalMessageDataFactory();
        DScope scope = fact.createDScope(message);
        Distillate d =  new Distillate(scope.primary());
        RectifierUnit unit = new RectifierUnit(_rectifier, d, true);
        List<ExecutionUnit> exec = new ArrayList<ExecutionUnit>();
        exec.add(unit);
        execute().synch(exec);
        ehandler.throwException(); //throw exception if it is present
        _alteredMessage = unit.finalDistillate();
        
	}

    protected void addSteps(Rectifier rectifier)
    {
		rectifier.addStep(new TranslationStage(translator.json));
        rectifier.addStep(new SanitizationStage());
        rectifier.addStep(new AlterationStage());
    }

    protected void addLastSteps(Rectifier rectifier)
    {
		rectifier.addStep(new StorageStage());
    }
	
	@Override
	public SCType scType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataInstincts instinct() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SCFactory creator() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void postMessage() throws CtxException {
		Rectifier rect = new Rectifier();
		//rect.addStep(new StorageStage());
        addLastSteps(rect);
		RectifierUnit unit = new RectifierUnit(rect, _alteredMessage, true);
		List<ExecutionUnit> exec = new ArrayList<ExecutionUnit>();
        exec.add(unit);
        execute().synch(exec);
	}

}
