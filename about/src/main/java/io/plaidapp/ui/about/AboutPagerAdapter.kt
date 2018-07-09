/*
 * Copyright 2018 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.plaidapp.ui.about

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.plaidapp.about.R
import io.plaidapp.core.util.HtmlUtils
import io.plaidapp.ui.about.uimodel.AboutUiModel
import java.security.InvalidParameterException

internal class AboutPagerAdapter(private val uiModel: AboutUiModel) : PagerAdapter() {

    private lateinit var aboutPlaid: View
    private lateinit var aboutIcon: View
    private lateinit var aboutLibs: View
    private lateinit var layoutInflater: LayoutInflater

    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        return getPage(position, collection).also {
            collection.addView(it)
        }
    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
    }

    override fun getCount() = 3

    override fun isViewFromObject(view: View, obj: Any) = view === obj

    private fun getPage(position: Int, parent: ViewGroup): View {
        return when (position) {
            0 -> getAboutAppPage(parent)
            1 -> getAboutIconPage(parent)
            2 -> getAboutLibsPage(parent)
            else -> throw InvalidParameterException()
        }
    }

    private fun getAboutIconPage(parent: ViewGroup): View {
        assureLayoutInflaterInitialized(parent.context)
        if (!::aboutIcon.isInitialized) {
            aboutIcon = layoutInflater.inflate(R.layout.about_icon, parent, false).apply {
                findViewById<TextView>(R.id.icon_description).apply {
                    HtmlUtils.setTextWithNiceLinks(this, uiModel.iconAboutText)
                }
            }
        }
        return aboutIcon
    }

    private fun getAboutAppPage(parent: ViewGroup): View {
        assureLayoutInflaterInitialized(parent.context)
        if (!::aboutPlaid.isInitialized) {
            aboutPlaid = layoutInflater.inflate(R.layout.about_plaid, parent, false)
                    .apply {
                        findViewById<TextView>(R.id.about_description).apply {
                            HtmlUtils.setTextWithNiceLinks(this, uiModel.appAboutText)
                        }
                    }
        }
        return aboutPlaid
    }

    private fun getAboutLibsPage(parent: ViewGroup): View {
        assureLayoutInflaterInitialized(parent.context)
        if (!::aboutLibs.isInitialized) {
            aboutLibs = layoutInflater.inflate(R.layout.about_libs, parent, false).apply {
                findViewById<RecyclerView>(R.id.libs_list).apply {
                    adapter = LibraryAdapter(uiModel.librariesUiModel)
                }
            }
        }
        return aboutLibs
    }

    private fun assureLayoutInflaterInitialized(context: Context) {
        if (!::layoutInflater.isInitialized) {
            layoutInflater = LayoutInflater.from(context)
        }
    }
}
