package com.eversis.friendlyurl.override;

import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.service.JournalArticleLocalServiceWrapper;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceWrapper;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.Validator;
import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Component;
import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component(
        immediate = true,
        property = {
        },
        service = ServiceWrapper.class
)
public class JournalArticleLocalServiceWrapperOverride extends JournalArticleLocalServiceWrapper {

        public JournalArticleLocalServiceWrapperOverride() {
                super(null);
        }

        @Override
        public JournalArticle addArticle(long userId, long groupId, long folderId, long classNameId, long classPK, String articleId,
                                         boolean autoArticleId, double version, Map<Locale, String> titleMap, Map<Locale, String> descriptionMap,
                                         Map<Locale, String> friendlyURLMap, String content, String ddmStructureKey, String ddmTemplateKey, String layoutUuid,
                                         int displayDateMonth, int displayDateDay, int displayDateYear, int displayDateHour, int displayDateMinute, int expirationDateMonth,
                                         int expirationDateDay, int expirationDateYear, int expirationDateHour, int expirationDateMinute, boolean neverExpire, int reviewDateMonth,
                                         int reviewDateDay, int reviewDateYear, int reviewDateHour, int reviewDateMinute, boolean neverReview, boolean indexable, boolean smallImage,
                                         String smallImageURL, File smallImageFile, Map<String, byte[]> images, String articleURL, ServiceContext serviceContext) throws PortalException {

                Locale locale = getArticleDefaultLocale(content);
                Map<Locale, String> editetdFriendlyMap = checkFriendlyURLMap(locale, friendlyURLMap, titleMap);

                for(Map.Entry<Locale, String> entry : editetdFriendlyMap.entrySet()){
                        entry.setValue(StringUtils.stripAccents(entry.getValue()));
                }

                return super.addArticle(userId, groupId, folderId, classNameId, classPK, articleId, autoArticleId, version, titleMap, descriptionMap, editetdFriendlyMap, content, ddmStructureKey, ddmTemplateKey, layoutUuid, displayDateMonth, displayDateDay, displayDateYear, displayDateHour, displayDateMinute, expirationDateMonth, expirationDateDay, expirationDateYear, expirationDateHour, expirationDateMinute, neverExpire, reviewDateMonth, reviewDateDay, reviewDateYear, reviewDateHour, reviewDateMinute, neverReview, indexable, smallImage, smallImageURL, smallImageFile, images, articleURL, serviceContext);
        }

        private Map<Locale, String> checkFriendlyURLMap(
                Locale defaultLocale, Map<Locale, String> friendlyURLMap,
                Map<Locale, String> titleMap) {

                for (Map.Entry<Locale, String> friendlyURL : friendlyURLMap.entrySet()) {

                        if (Validator.isNotNull(friendlyURL.getValue())) {
                                return friendlyURLMap;
                        }
                }

                Map<Locale, String> defaultFriendlyURLMap = new HashMap<>();
                defaultFriendlyURLMap.put(defaultLocale, titleMap.get(defaultLocale));

                return defaultFriendlyURLMap;
        }

        private Locale getArticleDefaultLocale(String content) {
                String defaultLanguageId = LocalizationUtil.getDefaultLanguageId(
                        content);

                if (Validator.isNotNull(defaultLanguageId)) {
                        return LocaleUtil.fromLanguageId(defaultLanguageId);
                }

                return LocaleUtil.getSiteDefault();
        }
}