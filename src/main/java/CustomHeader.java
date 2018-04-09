import java.util.Set;

public class CustomHeader {
    private String key;
    private Set<String> values;

    public CustomHeader(String key, Set<String> values) {
        this.key = key;
        this.values = values;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Set<String> getValues() {
        return values;
    }

    public void setValues(Set<String> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(key).append(": ");
        for (String s: values) {
            sb.append(s);
            sb.append(",");
        }
        if (sb.length() > 0) sb.deleteCharAt(sb.length()-1);

        return sb.toString();
    }
}
