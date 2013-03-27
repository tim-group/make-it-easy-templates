package com.youdevise.hip.testutils.datatemplate;

import com.natpryce.makeiteasy.Instantiator;
import com.natpryce.makeiteasy.MakeItEasy;
import com.natpryce.makeiteasy.Property;
import com.natpryce.makeiteasy.PropertyValue;

import java.util.ArrayList;
import java.util.List;

public abstract class DataTemplate<T, SELF extends DataTemplate<T, SELF>> {
    private final List<PropertyValue<T, ?>> values = new ArrayList<PropertyValue<T, ?>>();
    
    @SuppressWarnings("unchecked")
    public final <V> SELF with(Property<T, V> _property, V value) {
        values.add(MakeItEasy.with(_property, value));
        return (SELF) this;
    }
    
    @SuppressWarnings("unchecked")
    public final <V> SELF with(Property<T, V[]> _property, V...value) {
        values.add(MakeItEasy.with(_property, value));
        return (SELF) this;
    }
    
    @SuppressWarnings("unchecked")
    public final <V> SELF augmentedWithA(SELF additionalTemplate) {
        final DataTemplate<T, ?> extra = additionalTemplate;
        values.addAll(extra.values);
        return (SELF) this;
    }
    
    @SuppressWarnings("unchecked")
    protected final T make(Instantiator<T> type) {
        return (T)MakeItEasy.make(MakeItEasy.a(type, values.toArray(new PropertyValue[0])));
    }
    
    public abstract T build();
    
    public static <T, SELF extends DataTemplate<T, SELF>> T derivedFromA(DataTemplate<T, SELF> pattern) {
        return pattern.build();
    }
}