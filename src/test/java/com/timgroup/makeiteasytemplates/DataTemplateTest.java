package com.youdevise.hip.testutils.datatemplate;

import com.natpryce.makeiteasy.Instantiator;
import com.natpryce.makeiteasy.Property;
import com.natpryce.makeiteasy.PropertyLookup;

import org.junit.Test;

import static com.natpryce.makeiteasy.Property.newProperty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DataTemplateTest {

    private static class TestData {
        public final String name;
        public final String quest;
        public final String favouriteColour;
        
        public TestData(String name, String quest, String favouriteColour) {
            this.name = name;
            this.quest = quest;
            this.favouriteColour = favouriteColour;
        }
    }
    
    private static class TestDataTemplate extends DataTemplate<TestData, TestDataTemplate> {
        public static final Property<TestData, String> _name = newProperty();
        public static final Property<TestData, String> _quest = newProperty();
        public static final Property<TestData, String> _favouriteColour = newProperty();
        @Override
        public TestData build() {
            return make(new Instantiator<TestData>() {
                @Override
                public TestData instantiate(final PropertyLookup<TestData> lookup) {
                    return new TestData(lookup.valueOf(_name, "Abaddon"),
                                        lookup.valueOf(_quest, "To consume the cosmos"),
                                        lookup.valueOf(_favouriteColour, "Blacker than the blackest night"));
                }
            });
        }
    }
    
    @Test public void
    partial_templates_can_be_filled_in_later() {
        TestDataTemplate partialTemplate = new TestDataTemplate() {{
            with(_name, "Mr Fluffy");
            with(_quest, "To bring joy to the hearts of little children everywhere");
        }};
        
        TestData fluffy = DataTemplate.derivedFromA(partialTemplate.with(TestDataTemplate._favouriteColour, "Pink, thtupid!"));
        
        assertThat(fluffy.favouriteColour, is("Pink, thtupid!"));
    }
    
    @Test public void
    already_filled_in_properties_can_be_overridden() {
        TestDataTemplate partialTemplate = new TestDataTemplate() {{
            with(_name, "Yog Soggoth");
            with(_quest, "To collect souls for my cosmic larvae to feed upon");
            with(_favouriteColour, "There is no mortal name for my favourite colour");
        }};
        
        TestData yogSoggoth = DataTemplate.derivedFromA(partialTemplate.with(TestDataTemplate._favouriteColour, "Pink, thtupid!"));
        
        assertThat(yogSoggoth.favouriteColour, is("Pink, thtupid!"));
    }
    
    @Test public void
    templates_can_be_overridden_with_other_templates() {
        TestDataTemplate partialGoodTemplate = new TestDataTemplate() {{
            with(_name, "Mr Fluffy");
            with(_quest, "To bring joy to the hearts of little children everywhere");
        }};
    
        TestDataTemplate partialEvilTemplate = new TestDataTemplate() {{
            with(_quest, "To collect souls for my cosmic larvae to feed upon");
            with(_favouriteColour, "There is no mortal name for my favourite colour");
        }};
        
        TestData confusedEntity = DataTemplate.derivedFromA(partialGoodTemplate.augmentedWithA(partialEvilTemplate));
        
        assertThat(confusedEntity.name, is("Mr Fluffy"));
        assertThat(confusedEntity.quest, is("To collect souls for my cosmic larvae to feed upon"));
        assertThat(confusedEntity.favouriteColour, is("There is no mortal name for my favourite colour"));
    }
}
