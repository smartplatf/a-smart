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
 * File:                org.anon.smart.smcore.inbuilt.transition.TransitionServices
 * Author:              rsankar
 * Revision:            1.0
 * Date:                20-08-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A set of out of box services provided by Smart
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.inbuilt.transition;

import java.util.List;
import java.util.Properties;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeBodyPart;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;

import org.anon.smart.smcore.data.ConfigData;
import org.anon.smart.smcore.inbuilt.config.EmailConfig;
import org.anon.smart.smcore.config.ConfigService;
import org.anon.smart.smcore.inbuilt.responses.SuccessUpdated;

import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public class TransitionServices
{
    public TransitionServices()
    {
    }

    public void changeState()
        throws CtxException
    {
        System.out.println("This transition does not need to do anything. It will just change state.");
        new SuccessUpdated("Updated state");
    }

    public void sendEmailService(String to, String subject, String msg)
        throws CtxException
    {
        Class cls = EmailConfig.class;
        Class<? extends ConfigData> ccls = (Class<? extends ConfigData>)cls;
        Object cfg = ConfigService.configFor("EMAIL", ccls);
        EmailConfig ecfg = (EmailConfig)cfg;
        assertion().assertNotNull(ecfg, "Please setup an email config for key EMAIL before calling this service");
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "false");
        props.put("mail.smtp.host", ecfg.getSMTPHost());
        props.put("mail.smtp.port", ecfg.getSMTPPort());


        final String username = ecfg.getUserName();
        final String pwd = ecfg.getPassword();
        System.out.println("Got config as: " + props + ":" + username + ":" + pwd);
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, pwd);
                    }
                });

        assertion().assertNotNull(session, "Cannot create a email session. ");

        try 
        {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(ecfg.getUserName()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(msg);
            Transport.send(message);
        } 
        catch (MessagingException err) 
        {
            except().rt(err, new CtxException.Context("Exception", err.getMessage()));
        }
    }

    public void sendWithAttachment(String to, String subject, String msg, String filename, String name)
        throws CtxException
    {
        Class cls = EmailConfig.class;
        Class<? extends ConfigData> ccls = (Class<? extends ConfigData>)cls;
        Object cfg = ConfigService.configFor("EMAIL", ccls);
        EmailConfig ecfg = (EmailConfig)cfg;
        assertion().assertNotNull(ecfg, "Please setup an email config for key EMAIL before calling this service");
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "false");
        props.put("mail.smtp.host", ecfg.getSMTPHost());
        props.put("mail.smtp.port", ecfg.getSMTPPort());


        final String username = ecfg.getUserName();
        final String pwd = ecfg.getPassword();
        System.out.println("Got config as: " + props + ":" + username + ":" + pwd);
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, pwd);
                    }
                });

        assertion().assertNotNull(session, "Cannot create a email session. ");
        assertion().assertNotNull(to, "Need to send atleast one receipient");
        assertion().assertTrue((to.length() > 0), "Atleast one receipient is required to send email to.");

        try 
        {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(ecfg.getUserName()));
            InternetAddress[] addr = InternetAddress.parse(to);
            message.setRecipients(Message.RecipientType.BCC, addr);
            message.setSubject(subject);

            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(msg);
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(name);
            multipart.addBodyPart(messageBodyPart);

            // Send the complete message parts
            message.setContent(multipart);
            Transport.send(message);
        } 
        catch (MessagingException err) 
        {
            except().rt(err, new CtxException.Context("Exception", err.getMessage()));
        }
    }
}

