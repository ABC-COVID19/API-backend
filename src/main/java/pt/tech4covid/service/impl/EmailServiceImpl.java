package pt.tech4covid.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pt.tech4covid.domain.Newsletter;
import pt.tech4covid.service.EmailService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for email management.
 */
@Service
public class EmailServiceImpl implements EmailService {

    private final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private NewsletterServiceImpl newsletterService;

    public EmailServiceImpl() {
    }

    /**
     * Send newsletter email.
     */
    // TODO This config schedule.newsletter.cron should be replace by backoffice configs
    @Scheduled(cron = "${schedule.newsletter.cron}")
    @Override
    @Transactional(propagation=Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
    public void sendNewsletterEmail() {
        try {
            // Get all newsletters
            Page<Newsletter> newsletters = newsletterService.findAll(PageRequest.of(0, (int) newsletterService.count()));

            newsletters.stream().forEach(newsletter -> {

                try {
                    // To email
                    String toEmail = newsletter.getEmail();

                    // TODO Email subject
                    String subject = "ICAM Updates";

                    // List with the HTML text from all categories with revisions
                    List<String> categoryTreeList = new ArrayList<>();
                    newsletter.getCategoryTrees().stream().forEach(categoryTree -> {

                        // TODO Text with category identification
                        String categoryTreeText = StringUtils.join("<h2>", categoryTree.getItemName(), "</h2><br/>");

                        // TODO Text with the title of first revision of category
                        String firstRevisionText = categoryTree.getRevisions().stream().findFirst()
                            .map(revision -> StringUtils.join("<span>", revision.getTitle(), "</span>", "<br/>"))
                            .orElse("<br/>");

                        categoryTreeList.add(StringUtils.join(categoryTreeText, firstRevisionText));

                    });

                    // TODO Definition of email body
                    String textMessage = StringUtils.join("<h1>Hello ", newsletter.getFirstName(), StringUtils.SPACE,
                        newsletter.getLastName(), "!</h1><br/>", categoryTreeList.stream().collect(Collectors.joining()));

                    // Send email
                    sendHTMLEmail(toEmail, subject, "textMessage");

                } catch (Exception ex) {
                    // Exception when problem occur during the sending of email
                    log.error(StringUtils.join("Problem sending newsletter to ", newsletter.getEmail(), " - exception:", ex.getMessage()));
                }

            });
        } catch (Exception ex) {
            // Exception when problem occur at getting newsletters
            log.error(StringUtils.join("Problem getting newsletters info - exception:", ex.getMessage()));
        }
    }

    /**
     * Send email with HTML body
     *
     * @param toEmail     - email to send
     * @param subject     - subject of email
     * @param textMessage - body of email (String in HTML format)
     * @throws MessagingException
     */
    private void sendHTMLEmail(String toEmail, String subject, String textMessage) throws MessagingException {
        log.info("Sending email to: " + toEmail);

        MimeMessage msg = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(msg, false);

        helper.setTo(toEmail);

        helper.setSubject(subject);

        helper.setText(textMessage, true);

        javaMailSender.send(msg);

        log.info("Email sent to: " + toEmail);
    }
}
