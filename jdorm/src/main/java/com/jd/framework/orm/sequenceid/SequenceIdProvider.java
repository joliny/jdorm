package com.jd.framework.orm.sequenceid;

public interface SequenceIdProvider {

    public SequenceId create(String name);

    public SequenceId create(String name, int begin);

    public int load(String name);

    public void store(String name, int value);

}
