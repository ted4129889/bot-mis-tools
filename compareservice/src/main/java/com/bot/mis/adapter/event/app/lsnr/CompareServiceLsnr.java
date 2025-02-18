/* (C) 2025 */
package com.bot.mis.adapter.event.app.lsnr;

import com.bot.mis.adapter.event.app.evt.CompareService;
import com.bot.txcontrol.adapter.event.BatchListenerCase;
import com.bot.txcontrol.config.logger.ApLogHelper;
import com.bot.txcontrol.eum.LogType;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Slf4j
@Component("CompareServiceLsnr")
@Scope("prototype")
public class CompareServiceLsnr extends BatchListenerCase<CompareService> {

    //    @Async("batchThreadPoolTaskExecutor") // 如需平行處理請將此行註解拿掉
    @Override
    public void onApplicationEvent(CompareService event) {
        ApLogHelper.info(log, false, LogType.NORMAL.getCode(), "CompareServiceLsnr");
        this.beforRun(event);
    }

    @Override
    @SneakyThrows
    protected void run(CompareService event) {
        ApLogHelper.info(log, false, LogType.NORMAL.getCode(), "CompareServiceLsnr run()");
    }
}
