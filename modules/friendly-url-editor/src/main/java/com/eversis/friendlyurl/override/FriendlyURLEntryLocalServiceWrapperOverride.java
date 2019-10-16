package com.eversis.friendlyurl.override;

import com.liferay.friendly.url.model.FriendlyURLEntry;
import com.liferay.friendly.url.service.FriendlyURLEntryLocalServiceWrapper;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceWrapper;
import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Component;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component(
        immediate = true,
        property = {
        },
        service = ServiceWrapper.class
)
public class FriendlyURLEntryLocalServiceWrapperOverride extends FriendlyURLEntryLocalServiceWrapper {

    private static String DIACRITIC_PATTERN = "[^a-z0-9_-]+";

    public FriendlyURLEntryLocalServiceWrapperOverride() {
        super(null);
    }

    @Override
    public FriendlyURLEntry addFriendlyURLEntry(long groupId, long classNameId, long classPK, Map<String, String> urlTitleMap, ServiceContext serviceContext) throws PortalException {

        for(Map.Entry<String, String> entry : urlTitleMap.entrySet()){
            try {
                String decodedValue = URLDecoder.decode(entry.getValue(), StandardCharsets.UTF_8.toString());
                Pattern validFriendlyUrlPattern = Pattern.compile(DIACRITIC_PATTERN);
                Matcher friendlyUrlMatcher = validFriendlyUrlPattern.matcher(decodedValue);

                if (friendlyUrlMatcher.find()) {
                    decodedValue = StringUtils.stripAccents(decodedValue);
                    urlTitleMap.replace(entry.getKey(), decodedValue);
                    decodedValue = URLEncoder.encode(decodedValue, StandardCharsets.UTF_8.toString());
                    entry.setValue(decodedValue);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return super.addFriendlyURLEntry(groupId, classNameId, classPK, urlTitleMap, serviceContext);
    }
}