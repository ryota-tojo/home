package com.example.home.data.template

import com.example.home.data.category.FixtureCategory
import com.example.home.data.etc.FixtureEtc
import com.example.home.data.group.FixtureGroupList
import com.example.home.data.member.FixtureMember
import com.example.home.data.shopping.FixtureShopping
import com.example.home.domain.entity.template.ShoppingEntryTemplate
import com.example.home.domain.value_object.template.TemplateId
import com.example.home.domain.value_object.template.TemplateName
import com.example.home.domain.value_object.template.TemplateUseFlg
import com.example.home.domain.value_object.template.TmpId

class FixtureShoppingEntryTemplate {
    static ID_正常() {
        new TmpId(1)
    }

    static テンプレートID_正常() {
        new TemplateId("template_id")
    }

    static テンプレートID_バリエーションエラー() {
        new TemplateId("template,id")
    }

    static テンプレート名_正常() {
        new TemplateName("テンプレート名")
    }

    static テンプレート名_バリエーションエラー() {
        new TemplateName("テンプレート?名")
    }

    static 使用フラグ_使用() {
        new TemplateUseFlg(1)
    }


    static 購入データ入力テンプレート_正常() {
        return new ShoppingEntryTemplate(
                ID_正常(),
                FixtureGroupList.所属グループID_正常(),
                テンプレートID_正常(),
                テンプレート名_正常(),
                FixtureMember.メンバーID_正常(),
                FixtureCategory.カテゴリーID_正常(),
                FixtureShopping.購入種別_出費(),
                FixtureShopping.支払い方法_現金(),
                FixtureShopping.精算状態_未精算(),
                FixtureEtc.金額_正常(),
                FixtureShopping.備考_正常(),
                使用フラグ_使用()
        )
    }
}
