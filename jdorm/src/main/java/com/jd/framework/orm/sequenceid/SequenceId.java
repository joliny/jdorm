package com.jd.framework.orm.sequenceid;

public class SequenceId {
    public static final int NOT_FOUND = 0;
    private final SequenceIdProvider provider;
    private final String name;
    private final int beginValue;
    private int value;

    protected SequenceId(SequenceIdProvider provider, String name, int beginValue) {
        this.provider = provider;
        this.name = name;
        this.beginValue = beginValue;
        this.value = -1;

        if (beginValue <= 0) {
            throw new IllegalArgumentException("begin value must be great than zero.");
        }
    }

    public String getName() {
        return name;
    }

    public synchronized int nextVal() {
    	
        if (value < 0) {
        	
            value = provider.load(name);
            if (value <= NOT_FOUND) {
                value = beginValue - 1;
            }
            value++;
            provider.store(name, value);
        }
        return value;
    }
}
