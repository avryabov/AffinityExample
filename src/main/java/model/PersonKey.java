package model;

import org.apache.ignite.cache.affinity.AffinityKeyMapped;

public class PersonKey {

    private Integer perId;

    @AffinityKeyMapped
    private Integer comId;

    public PersonKey(Integer perId, Integer comId) {
        this.perId = perId;
        this.comId = comId;
    }

    public Integer getPerId() {
        return perId;
    }

    public Integer getComId() {
        return comId;
    }

    @Override public String toString() {
        return "PersonKey{" +
            "perId=" + perId +
            ", comId=" + comId +
            '}';
    }
}
