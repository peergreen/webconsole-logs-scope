/**
 * Peergreen S.A.S. All rights reserved.
 * Proprietary and confidential.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.peergreen.webconsole.scope.logs.stream;

import com.peergreen.kernel.system.StreamType;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;

public class TextColumnGenerator implements Table.ColumnGenerator {

    /**
     *
     */
    private static final long serialVersionUID = 954990613519409234L;

    /**
     * Generates the cell containing the Date value. The column is
     * irrelevant in this use case.
     */
    @Override
    public Component generateCell(Table source, Object itemId,
            Object columnId) {

        BeanItem<TableEntry> item = (BeanItem<TableEntry>) source.getItem(itemId);
        TableEntry tableEntry = item.getBean();
        Property<?> prop = item.getItemProperty(columnId);
        Label label = new Label();

        String text = item.getBean().getText().replaceAll(" ", "&nbsp;");

        if (StreamType.OUT.name().equals(item.getBean().getType()) || "INFO".equals(item.getBean().getType())) {
            label.setValue("<font color='black'>".concat(text).concat("</font>"));
        } else if (StreamType.ERR.name().equals(item.getBean().getType()) || "ERROR".equals(item.getBean().getType())) {
            label.setValue("<font color='red'>".concat(text).concat("</font>"));
        } else if ("DEBUG".equals(item.getBean().getType())) {
            label.setValue("<font color='grey'>".concat(text).concat("</font>"));
        } else if ("WARNING".equals(item.getBean().getType())) {
            label.setValue("<font color='orange'>".concat(text).concat("</font>"));
        } else if ("SEVERE".equals(item.getBean().getType())) {
            label.setValue("<font color='red'>".concat(text).concat("</font>"));
        } else {
            label.setValue("<font color='black'>".concat(text).concat("</font>"));
        }

        label.setValue("<div style='font-family:Monaco, Menlo, Consolas, monospace;font-size:small;'>".concat(label.getValue().concat("</div>")));

        label.setContentMode(ContentMode.HTML);
        return label;

    }

}
