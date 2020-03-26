package pt.tech4covid.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pt.tech4covid.web.rest.TestUtil;

public class NewsletterTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Newsletter.class);
        Newsletter newsletter1 = new Newsletter();
        newsletter1.setId(1L);
        Newsletter newsletter2 = new Newsletter();
        newsletter2.setId(newsletter1.getId());
        assertThat(newsletter1).isEqualTo(newsletter2);
        newsletter2.setId(2L);
        assertThat(newsletter1).isNotEqualTo(newsletter2);
        newsletter1.setId(null);
        assertThat(newsletter1).isNotEqualTo(newsletter2);
    }
}
