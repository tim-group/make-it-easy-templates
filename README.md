Make-it-easy Templates
======================

A trivial extension to Nat Pryce's [make-it-easy](https://code.google.com/p/make-it-easy/) library allowing data templates to be defined so that test data can be build using an anonymous block within an anonymous extension class, similar to the style of [JMock](http://jmock.org/cheat-sheet.html) expectations.

Define your Apple template like this:
```java
public class AppleTemplate extends DataTemplate<Apple, AppleTemplate> {
  public static final Property<Fruit, Double> ripeness = newProperty();
  public static final Property<Apple, Integer> leaves = newProperty();

  @Override
  public Apple build() {
    return make(new Instantiator<Apple>() {
      @Override
      public Apple instantiate(PropertyLookup<Apple> lookup) {
        Apple apple = new Apple(lookup.valueOf(leaves, 2));
        apple.ripen(lookup.valueOf(ripeness, 0.0));
        return apple;
      }
    });
  }
}
```

Then you can make Apple instances like this:
```java
DataTemplate.derivedFromA(new AppleTemplate() {{
  with(ripeness, 1.0);
  with(leaves, 2);
}});
```

