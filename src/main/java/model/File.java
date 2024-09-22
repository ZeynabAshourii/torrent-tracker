package model;

import lombok.*;
import lombok.Setter;

import java.util.ArrayList;

public class File extends Bean {
    public ArrayList<Log> logs = new ArrayList<>();

    @Getter @Setter
    private String name;

    public File(long ID, String name) {
        super(ID);
        this.name = name;
    }

    public static final class FileBuilder {
        private long ID;
        private String name;

        private FileBuilder() {
        }

        public static FileBuilder aFile() {
            return new FileBuilder();
        }

        public FileBuilder withID(long ID) {
            this.ID = ID;
            return this;
        }

        public FileBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public File build() {
            return new File(ID, name);
        }
    }
}
