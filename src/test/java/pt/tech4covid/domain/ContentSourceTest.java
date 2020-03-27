package pt.tech4covid.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pt.tech4covid.web.rest.TestUtil;

public class ContentSourceTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContentSource.class);
        ContentSource contentSource1 = new ContentSource();
        contentSource1.setId(1L);
        ContentSource contentSource2 = new ContentSource();
        contentSource2.setId(contentSource1.getId());
        assertThat(contentSource1).isEqualTo(contentSource2);
        contentSource2.setId(2L);
        assertThat(contentSource1).isNotEqualTo(contentSource2);
        contentSource1.setId(null);
        assertThat(contentSource1).isNotEqualTo(contentSource2);
    }
}
