package pt.tech4covid.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pt.tech4covid.web.rest.TestUtil;

public class PublicationSourceTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PublicationSource.class);
        PublicationSource publicationSource1 = new PublicationSource();
        publicationSource1.setId(1L);
        PublicationSource publicationSource2 = new PublicationSource();
        publicationSource2.setId(publicationSource1.getId());
        assertThat(publicationSource1).isEqualTo(publicationSource2);
        publicationSource2.setId(2L);
        assertThat(publicationSource1).isNotEqualTo(publicationSource2);
        publicationSource1.setId(null);
        assertThat(publicationSource1).isNotEqualTo(publicationSource2);
    }
}
