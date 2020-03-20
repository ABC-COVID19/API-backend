import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Abccovid19SharedModule } from 'app/shared/shared.module';
import { RevisionComponent } from './revision.component';
import { RevisionDetailComponent } from './revision-detail.component';
import { RevisionUpdateComponent } from './revision-update.component';
import { RevisionDeleteDialogComponent } from './revision-delete-dialog.component';
import { revisionRoute } from './revision.route';

@NgModule({
  imports: [Abccovid19SharedModule, RouterModule.forChild(revisionRoute)],
  declarations: [RevisionComponent, RevisionDetailComponent, RevisionUpdateComponent, RevisionDeleteDialogComponent],
  entryComponents: [RevisionDeleteDialogComponent]
})
export class Abccovid19RevisionModule {}
