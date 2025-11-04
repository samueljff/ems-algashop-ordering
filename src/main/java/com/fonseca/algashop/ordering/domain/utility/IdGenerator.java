package com.fonseca.algashop.ordering.domain.utility;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedEpochRandomGenerator;
import io.hypersistence.tsid.TSID;

import java.util.UUID;

public class IdGenerator {

    private static final TimeBasedEpochRandomGenerator timeBasedEpochRandomGenerator = Generators.timeBasedEpochRandomGenerator();

    private static final TSID.Factory tsidFactory = TSID.Factory.INSTANCE;

    private IdGenerator(){
    }

    public static UUID generateTimeBasedUUID(){
        return timeBasedEpochRandomGenerator.generate();
    }

    /*
     *Para Produção necessário variaveis de ambientes:
     *TSID_NODE
     * TSID_NODE_COUNT
     */
    public static TSID generateTSID(){
        return tsidFactory.generate();
    }
}