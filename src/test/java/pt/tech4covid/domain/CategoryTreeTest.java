package pt.tech4covid.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pt.tech4covid.web.rest.TestUtil;

public class CategoryTreeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategoryTree.class);
        CategoryTree categoryTree1 = new CategoryTree();
        categoryTree1.setId(1L);
        CategoryTree categoryTree2 = new CategoryTree();
        categoryTree2.setId(categoryTree1.getId());
        assertThat(categoryTree1).isEqualTo(categoryTree2);
        categoryTree2.setId(2L);
        assertThat(categoryTree1).isNotEqualTo(categoryTree2);
        categoryTree1.setId(null);
        assertThat(categoryTree1).isNotEqualTo(categoryTree2);
    }
}
