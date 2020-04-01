package pt.tech4covid.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pt.tech4covid.web.rest.TestUtil;

public class SourceRepoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SourceRepo.class);
        SourceRepo sourceRepo1 = new SourceRepo();
        sourceRepo1.setId(1L);
        SourceRepo sourceRepo2 = new SourceRepo();
        sourceRepo2.setId(sourceRepo1.getId());
        assertThat(sourceRepo1).isEqualTo(sourceRepo2);
        sourceRepo2.setId(2L);
        assertThat(sourceRepo1).isNotEqualTo(sourceRepo2);
        sourceRepo1.setId(null);
        assertThat(sourceRepo1).isNotEqualTo(sourceRepo2);
    }
}
