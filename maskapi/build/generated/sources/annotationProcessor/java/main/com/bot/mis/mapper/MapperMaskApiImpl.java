package com.bot.mis.mapper;

import com.bot.mis.adapter.in.api.CL_BATCH_I;
import com.bot.mis.adapter.in.svc.MaskService_I;
import com.bot.txcontrol.adapter.RequestApiLabelCase;
import com.bot.txcontrol.adapter.in.RequestLabel;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-17T16:29:48+0800",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.5.jar, environment: Java 21.0.6 (Amazon.com Inc.)"
)
@Component
public class MapperMaskApiImpl implements MapperMaskApi {

    @Override
    public MaskService_I mapperMaskService(CL_BATCH_I cL_BATCH_I) {
        if ( cL_BATCH_I == null ) {
            return null;
        }

        MaskService_I maskService_I = new MaskService_I();

        maskService_I.setLabel( requestApiLabelCaseToRequestLabel( cL_BATCH_I.getLabel() ) );

        return maskService_I;
    }

    protected RequestLabel requestApiLabelCaseToRequestLabel(RequestApiLabelCase requestApiLabelCase) {
        if ( requestApiLabelCase == null ) {
            return null;
        }

        RequestLabel requestLabel = new RequestLabel();

        requestLabel.setExcutStatus( requestApiLabelCase.getExcutStatus() );
        requestLabel.setRevFg( requestApiLabelCase.getRevFg() );
        requestLabel.setUserId( requestApiLabelCase.getUserId() );
        requestLabel.setPageNo( requestApiLabelCase.getPageNo() );
        requestLabel.setPageSize( requestApiLabelCase.getPageSize() );
        requestLabel.setKinbr( requestApiLabelCase.getKinbr() );
        requestLabel.setTrmseq( requestApiLabelCase.getTrmseq() );
        requestLabel.setTxtno( requestApiLabelCase.getTxtno() );
        requestLabel.setOrgkin( requestApiLabelCase.getOrgkin() );
        requestLabel.setOrgtrm( requestApiLabelCase.getOrgtrm() );
        requestLabel.setOrgtno( requestApiLabelCase.getOrgtno() );
        requestLabel.setTtskid( requestApiLabelCase.getTtskid() );
        requestLabel.setTrmtyp( requestApiLabelCase.getTrmtyp() );
        requestLabel.setTlrno( requestApiLabelCase.getTlrno() );
        requestLabel.setTxcode( requestApiLabelCase.getTxcode() );
        requestLabel.setRstinq( requestApiLabelCase.getRstinq() );
        requestLabel.setPtype( requestApiLabelCase.getPtype() );
        requestLabel.setDscpt( requestApiLabelCase.getDscpt() );
        requestLabel.setMrkey( requestApiLabelCase.getMrkey() );
        requestLabel.setTxtype( requestApiLabelCase.getTxtype() );
        requestLabel.setCrdb( requestApiLabelCase.getCrdb() );
        requestLabel.setSpcd( requestApiLabelCase.getSpcd() );
        requestLabel.setNbcd( requestApiLabelCase.getNbcd() );
        requestLabel.setHcode( requestApiLabelCase.getHcode() );
        requestLabel.setTrnmod( requestApiLabelCase.getTrnmod() );
        requestLabel.setSbtmod( requestApiLabelCase.getSbtmod() );
        requestLabel.setCurcd( requestApiLabelCase.getCurcd() );
        requestLabel.setTxamt( requestApiLabelCase.getTxamt() );
        requestLabel.setFepdd( requestApiLabelCase.getFepdd() );
        requestLabel.setPredo( requestApiLabelCase.getPredo() );
        requestLabel.setCaldy( requestApiLabelCase.getCaldy() );
        requestLabel.setCaltm( requestApiLabelCase.getCaltm() );
        requestLabel.setTotafg( requestApiLabelCase.getTotafg() );
        requestLabel.setWarnfg( requestApiLabelCase.getWarnfg() );
        requestLabel.setSupno( requestApiLabelCase.getSupno() );
        requestLabel.setMttpseq( requestApiLabelCase.getMttpseq() );
        requestLabel.setPseudo( requestApiLabelCase.getPseudo() );
        requestLabel.setCokey( requestApiLabelCase.getCokey() );
        requestLabel.setCobkno( requestApiLabelCase.getCobkno() );
        requestLabel.setVer( requestApiLabelCase.getVer() );
        requestLabel.setAcbrno( requestApiLabelCase.getAcbrno() );
        requestLabel.setSecno( requestApiLabelCase.getSecno() );
        requestLabel.setIbffg( requestApiLabelCase.getIbffg() );
        requestLabel.setJobno( requestApiLabelCase.getJobno() );
        requestLabel.setSbcash( requestApiLabelCase.getSbcash() );
        requestLabel.setCldept( requestApiLabelCase.getCldept() );
        requestLabel.setTlrempno( requestApiLabelCase.getTlrempno() );
        requestLabel.setSupempno( requestApiLabelCase.getSupempno() );
        requestLabel.setBiosetlno( requestApiLabelCase.getBiosetlno() );

        return requestLabel;
    }
}
