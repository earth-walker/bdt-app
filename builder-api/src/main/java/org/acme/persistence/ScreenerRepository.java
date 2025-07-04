package org.acme.persistence;

import org.acme.model.Screener;

import java.util.List;
import java.util.Optional;

public interface ScreenerRepository {

    public List<Screener> getScreeners(String userId);

    public Optional<Screener> getScreener(String screenerId);

    public Optional<Screener> getScreenerMetaDataOnly(String screenerId);

    public String saveNewScreener(Screener screener) throws Exception;

    public void updateScreener(Screener screener) throws Exception;

    public void deleteScreener(String screenerId) throws Exception;

}
