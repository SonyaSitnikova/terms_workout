package ru.ominit;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.ominit.model.Haystack;
import ru.ominit.model.NoHaystacksException;

import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Created by Александр on 31.03.2018.
 */
@Service
public class RiddleLoader {
    private Logger logger = LoggerFactory.getLogger(RiddleLoader.class);
    private XmlMapper mapper = new XmlMapper();

    public RiddleLoader(){
        this.haystacksPath = "resources/haystacks";
    }

    public RiddleLoader(String haystacksPath){
        this.haystacksPath = haystacksPath;
    }

    private String haystacksPath;

    public String getAnyHaystackId(Random rnd){
        logger.debug("Pick random haystackId");
        File haystacksDirectory = new File(haystacksPath);
        String[] haystackFilenames = haystacksDirectory.list();
        if (haystackFilenames == null){
            logger.error("Directory was empty");
            throw new NoHaystacksException();
        }
        int nextId = rnd.nextInt(haystackFilenames.length);
        return haystackFilenames[nextId].replace(".xml", "");
    }

    public Haystack load(String sphinxFilename) throws IOException {
        logger.debug("Load haystack {}", sphinxFilename);
        File sphinxPath = new File(haystacksPath + "/" + sphinxFilename + ".xml");
        return mapper.readValue(sphinxPath, Haystack.class);
    }
}
