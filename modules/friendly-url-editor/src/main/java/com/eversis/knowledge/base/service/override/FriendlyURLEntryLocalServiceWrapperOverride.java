package com.eversis.knowledge.base.service.override;

import com.liferay.friendly.url.model.FriendlyURLEntry;
import com.liferay.friendly.url.service.FriendlyURLEntryLocalServiceWrapper;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceWrapper;
import org.osgi.service.component.annotations.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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

    static String DIACRITIC_PATTERN = "[^a-z0-9_-]+";

    public FriendlyURLEntryLocalServiceWrapperOverride() {
        super(null);
    }

    // knowledge base i configuration nie wywołują żadnej wersji addFriendlyURLEntry

    @Override
    public FriendlyURLEntry addFriendlyURLEntry(FriendlyURLEntry friendlyURLEntry) {
        System.out.println("1");
        return super.addFriendlyURLEntry(friendlyURLEntry);
    }

    //blogs
    @Override
    public FriendlyURLEntry addFriendlyURLEntry(long groupId, Class<?> clazz, long classPK, String urlTitle, ServiceContext serviceContext) throws PortalException {

        try {
            String tempUrl = URLDecoder.decode(urlTitle, StandardCharsets.UTF_8.toString());
            Pattern _validFriendlyUrlPattern = Pattern.compile(DIACRITIC_PATTERN);
            Matcher matcher = _validFriendlyUrlPattern.matcher(tempUrl);

            if (matcher.find()) {
                System.out.println("non-diacritic characters found");
                tempUrl = tempUrl.replaceAll(DIACRITIC_PATTERN, "-");
                return super.addFriendlyURLEntry(groupId, clazz, classPK, tempUrl, serviceContext);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return super.addFriendlyURLEntry(groupId, clazz, classPK, urlTitle, serviceContext);
    }

    @Override
    public FriendlyURLEntry addFriendlyURLEntry(long groupId, long classNameId, long classPK, Map<String, String> urlTitleMap, ServiceContext serviceContext) throws PortalException {

        for(Map.Entry<String, String> entry : urlTitleMap.entrySet()){
            try {
                String tempValue = URLDecoder.decode(entry.getValue(), StandardCharsets.UTF_8.toString());
                Pattern _validFriendlyUrlPattern = Pattern.compile(DIACRITIC_PATTERN);
                Matcher matcher = _validFriendlyUrlPattern.matcher(tempValue);

                if (matcher.find()) {
                    System.out.println("non-diacritic characters found");
                    tempValue = tempValue.replaceAll(DIACRITIC_PATTERN, "-");
                    urlTitleMap.replace(entry.getKey(), tempValue);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return super.addFriendlyURLEntry(groupId, classNameId, classPK, urlTitleMap, serviceContext);
    }

    @Override
    public FriendlyURLEntry addFriendlyURLEntry(long groupId, long classNameId, long classPK, String defaultLanguageId, Map<String, String> urlTitleMap, ServiceContext serviceContext) throws PortalException {
        System.out.println("4");

        return super.addFriendlyURLEntry(groupId, classNameId, classPK, defaultLanguageId, urlTitleMap, serviceContext);
    }

    @Override
    public FriendlyURLEntry addFriendlyURLEntry(long groupId, long classNameId, long classPK, String urlTitle, ServiceContext serviceContext) throws PortalException {
        System.out.println("5");

        return super.addFriendlyURLEntry(groupId, classNameId, classPK, urlTitle, serviceContext);
    }
}