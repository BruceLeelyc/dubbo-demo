package com.bitfty.mall.service;

import com.bitfty.mall.dao.NoticeDao;
import com.bitfty.mall.dto.NoticeDto;
import com.bitfty.mall.entity.Notice;
import com.bitfty.util.BeanMapper;
import com.bitfty.util.DateUtil;
import com.bitfty.util.Page;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service(timeout = 3000)
public class NoticeServiceImpl implements NoticeService {

    private static final Logger logger = LoggerFactory.getLogger(NoticeServiceImpl.class);

    @Autowired
    private NoticeDao noticeDao;

    @Override
    public Page<NoticeDto> page(Integer pageNo, Integer pageSize) {
        logger.info("tracd_id test test ~~~~~~~~~~~~~~~~~~~~~~~~");
        Map<String,Object> params = Maps.newHashMap();
        params.put("start",(pageNo-1)*pageSize);
        params.put("ends",pageSize);
        params.put("createAtEnd", DateUtil.getTime());
        params.put("expireTimeStart",DateUtil.getTime());
        List<Notice> notices = noticeDao.findPageByParams(params);
        Integer total = noticeDao.countByParams(params);
        return new Page(total,pageSize,pageNo, BeanMapper.mapList(notices, NoticeDto.class));
    }

    @Override
    public Page<NoticeDto> pageParams(Integer pageNo, Integer pageSize,Map<String,Object> params) {
        params.put("start",(pageNo-1)*pageSize);
        params.put("ends",pageSize);
        List<Notice> notices = noticeDao.findPageByParams(params);
        Integer total = noticeDao.countByParams(params);
        return new Page(total,pageSize,pageNo, BeanMapper.mapList(notices, NoticeDto.class));
    }

    @Override
    public NoticeDto info(Long id) {
        if (id == null){
            return null;
        }
        Notice notice = noticeDao.findById(id);
        return BeanMapper.map(notice,NoticeDto.class);
    }

    @Override
    public List<NoticeDto> list() {
        List<Notice> notice = noticeDao.list();
        return BeanMapper.mapList(notice,NoticeDto.class);
    }

    @Override
    public int update(NoticeDto noticeDto) {
        log.info("---noticeDto getId:"+noticeDto.getId());
        Notice notice = BeanMapper.map(noticeDto,Notice.class);
        log.info("---notice getId:"+notice.getId());
        return noticeDao.update(notice);
    }

    @Override
    public int save(NoticeDto noticeDto) {
        Notice notice = BeanMapper.map(noticeDto,Notice.class);
        return noticeDao.save(notice);
    }

    @Override
    public int delete(Long id) {
        return noticeDao.delete(id);
    }


}
