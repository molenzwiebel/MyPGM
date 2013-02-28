package nl.thijsmolendijk.MyPGM;

public class Pair {
    private final Object m_first;
    private final Object m_second;

    public Pair(Object found, Object id) {
        m_first = found;
        m_second = id;
    }

    public Pair(boolean found, String id) {
    	m_first = found;
        m_second = id;
	}

	public Object bool() {
        return m_first;
    }

    public Object core() {
        return m_second;
    }
}
