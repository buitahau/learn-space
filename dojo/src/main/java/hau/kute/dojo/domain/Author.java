package hau.kute.dojo.domain;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

public class Author {

    @Field(type = FieldType.Text)
    private String name;

    public Author() {}

    public Author(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Author{" + "name='" + name + '\'' + '}';
    }
}
