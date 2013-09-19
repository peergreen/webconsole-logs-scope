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

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;

public class TypeFilter implements Container.Filter {
    private static final long serialVersionUID = 6606181786787976311L;

    private static final String PROPERTY = "type";

    private boolean acceptSystemOut = true;
    private boolean acceptSystemErr = true;

    private boolean acceptLoggerWarning = true;
    private boolean acceptLoggerInfo = true;
    private boolean acceptLoggerError = true;


    public void setAcceptSystemOut(boolean acceptSystemOut) {
        this.acceptSystemOut = acceptSystemOut;
    }

    public void setAcceptSystemErr(boolean acceptSystemErr) {
        this.acceptSystemErr = acceptSystemErr;
    }

    public void setAcceptLoggerWarning(boolean acceptLoggerWarning) {
        this.acceptLoggerWarning = acceptLoggerWarning;
    }

    public void setAcceptLoggerInfo(boolean acceptLoggerInfo) {
        this.acceptLoggerInfo = acceptLoggerInfo;
    }

    public void setAcceptLoggerError(boolean acceptLoggerError) {
        this.acceptLoggerError = acceptLoggerError;
    }

    /** Apply the filter on an item to check if it passes. */
    @Override
    public boolean passesFilter(Object itemId, Item item)
            throws UnsupportedOperationException {

        Property<?> p = item.getItemProperty(PROPERTY);

        String value = (String) p.getValue();
        if ("OUT".equals(value)) {
            return acceptSystemOut;
        }
        if ("ERR".equals(value)) {
            return acceptSystemErr;
        }
        if ("INFO".equals(value)) {
            return acceptLoggerInfo;
        }
        if ("WARNING".equals(value)) {
            return acceptLoggerWarning;
        }
        if ("SEVERE".equals(value)) {
            return acceptLoggerError;
        }



        return true;
    }

    @Override
    public boolean appliesToProperty(Object propertyId) {
        return propertyId != null &&  propertyId.equals(PROPERTY);
    }

}
