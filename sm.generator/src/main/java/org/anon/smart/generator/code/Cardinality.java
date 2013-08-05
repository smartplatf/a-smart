
package org.anon.smart.generator.code;

/**
 * Cardinality of an attribute or association.
 *
 * 
 */
public enum Cardinality
{

    /**
     * A cardinality of class or interface --&gt; attribute or association target <code>[0..1]</code>.
     */
    ZeroToOne,

    /**
     * A cardinality of class or interface --&gt; attribute or association target <code>[1]</code>.
     */
    StrictlyOne,

    /**
     * A cardinality of class or interface --&gt; attribute or association target <code>[0..*]</code>.
     */
    ZeroToMany,

    /**
     * A cardinality of class or interface --&gt; attribute or association target <code>[1..*]</code>.
     */
    OneToMany
};
