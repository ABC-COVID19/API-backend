package pt.tech4covid.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.javamail.JavaMailSender;
import pt.tech4covid.domain.CategoryTree;
import pt.tech4covid.domain.Newsletter;
import pt.tech4covid.domain.Revision;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class EmailServiceImplTest {

    @InjectMocks
    EmailServiceImpl emailService;

    @Mock
    JavaMailSender javaMailSender;

    @Mock
    NewsletterServiceImpl newsletterService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void sendNewsletterEmailTest() {

        Set<Revision> revisions = new HashSet<>();
        Revision revision = new Revision();
        revision.setTitle("Revision title");
        revisions.add(revision);

        Set<CategoryTree> categoryTrees = new HashSet<>();
        CategoryTree categoryTree = new CategoryTree();
        categoryTree.setItemName("Category A");
        categoryTree.setRevisions(revisions);
        categoryTrees.add(categoryTree);

        Newsletter newsletter = new Newsletter();
        newsletter.setEmail("teste@gmail.com");
        newsletter.setFirstName("Teste");
        newsletter.setLastName("ABC");
        newsletter.setCategoryTrees(categoryTrees);

        Page<Newsletter> newsletters = new PageImpl<>(Arrays.asList(newsletter));

        Mockito.when(newsletterService.count()).thenReturn((long) 1);
        Mockito.when(newsletterService.findAll(PageRequest.of(0, 1))).thenReturn(newsletters);

        MimeMessage msg = new MimeMessage((Session) null);
        Mockito.when(javaMailSender.createMimeMessage()).thenReturn(msg);

        //test
        emailService.sendNewsletterEmail();

        Mockito.verify(newsletterService, Mockito.times(1)).count();
        Mockito.verify(newsletterService, Mockito.times(1)).findAll(PageRequest.of(0, 1));
        Mockito.verifyNoMoreInteractions(newsletterService);
        Mockito.verify(javaMailSender, Mockito.times(1)).createMimeMessage();
        Mockito.verify(javaMailSender, Mockito.times(1)).send(msg);
        Mockito.verifyNoMoreInteractions(javaMailSender);
    }
}
